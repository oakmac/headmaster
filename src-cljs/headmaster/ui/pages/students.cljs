(ns headmaster.ui.pages.students
  "Students page"
  (:require
    [clojure.string :as str]
    [headmaster.ui.constants :refer [ellipsis]]
    [headmaster.ui.html.common :as common]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall oget]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Sorting
;; TODO: move sorting logic to it's own namespace

(def stoplight-map
  {"green" 1
   "yellow" 2
   "red" 3})

(defn- compare-stoplight [a b]
  (compare (get stoplight-map a)
           (get stoplight-map b)))

(defn- compare-github [a b]
  (compare (util/safe-lower-case a)
           (util/safe-lower-case b)))

;;------------------------------------------------------------------------------
;; Subscriptions

(def default-sort-col :name)
(def default-sort-dir :asc)

(rf/reg-sub
  :students-page/sort-col
  (fn [db _]
    (get-in db [:students-page :sort-col] default-sort-col)))

(rf/reg-sub
  :students-page/sort-dir
  (fn [db _]
    (get-in db [:students-page :sort-dir] default-sort-dir)))

(rf/reg-sub
  :students-page/search-txt
  (fn [db _]
    (get-in db [:students-page :search-txt] "")))

(rf/reg-sub
  :students-vec
  :<- [:class]
  (fn [class _]
    (mapv
      (fn [[id student]]
        (assoc student :id (name id)))
      (:students class))))

(rf/reg-sub
  :filtered-students
  :<- [:students-vec]
  :<- [:students-page/search-txt]
  (fn [[students-vec search-txt] _]
    (let [lc-search-txt (str/lower-case search-txt)
          filter-fn (if (str/blank? search-txt)
                      (constantly true)
                      (fn [{:keys [github name shortName]}]
                        (or (str/includes? (util/safe-lower-case name) lc-search-txt)
                            (str/includes? (util/safe-lower-case shortName) lc-search-txt)
                            (str/includes? (util/safe-lower-case github) lc-search-txt))))]
      (filter filter-fn students-vec))))

(rf/reg-sub
  :sorted-and-filtered-students
  :<- [:filtered-students]
  :<- [:students-page/sort-col]
  :<- [:students-page/sort-dir]
  :<- [:students-page/search-txt]
  (fn [[students-vec sort-col sort-dir search-txt] _]
    (let [compare-fn (cond
                       (= sort-col :stoplight) compare-stoplight
                       (= sort-col :github) compare-github
                       :else compare)
          sorted-students (sort-by sort-col compare-fn students-vec)]
      (if (= sort-dir :desc)
        (reverse sorted-students)
        sorted-students))))

(def default-view-type "TILES_VIEW")

(rf/reg-sub
  :students-page/view-type
  (fn [db _]
    (get-in db [:students-page :view-type] default-view-type)))

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :students-page/init
  (fn [db _]
    (assoc db :page "STUDENTS_PAGE")))

(rf/reg-event-db
  :students-page/set-sort-col
  (fn [db [_ sort-col]]
    (assoc-in db [:students-page :sort-col] sort-col)))

(rf/reg-event-db
  :students-page/set-sort-dir
  (fn [db [_ sort-dir]]
    (assoc-in db [:students-page :sort-dir] sort-dir)))

(rf/reg-event-db
  :students-page/set-search-txt
  (fn [db [_ search-txt]]
    (assoc-in db [:students-page :search-txt] search-txt)))

(rf/reg-event-db
  :students-page/flip-sort-dir
  (fn [db _]
    (update-in db [:students-page :sort-dir]
      (fn [direction]
        (if (= direction :desc)
          :asc
          :desc)))))

(rf/reg-event-db
  :students-page/set-view-type
  (fn [db [_ view-type]]
    (assoc-in db [:students-page :view-type] view-type)))

;;------------------------------------------------------------------------------
;; Views

(defn- click-column-header [col-kwd current-sort-col current-sort-dir]
  (if (= col-kwd current-sort-col)
    (rf/dispatch [:students-page/flip-sort-dir])
    (do
      (rf/dispatch [:students-page/set-sort-col col-kwd])
      (rf/dispatch [:students-page/set-sort-dir :asc]))))

(defn HeaderColumn [{:keys [class col-kwd label]}]
  (let [sort-col @(rf/subscribe [:students-page/sort-col])
        sort-dir @(rf/subscribe [:students-page/sort-dir])]
    [:th
      {:class (when (string? class) class)
       :on-click #(click-column-header col-kwd sort-col sort-dir)}
      label
      (when (= sort-col col-kwd)
        (if (= sort-dir :asc)
          [common/SVGIcon "chevron-down" "sort-icon"]
          [common/SVGIcon "chevron-up" "sort-icon"]))]))

(defn THead []
  [:thead
    [:tr
      [HeaderColumn {:label "Status"
                     :col-kwd :stoplight
                     :class "stoplight-column"}]
      [HeaderColumn {:label "Name"
                     :col-kwd :name}]
      [HeaderColumn {:label "GitHub"
                     :col-kwd :github}]
      [HeaderColumn {:label "Last Commit"
                     :col-kwd :lastGitHubCommit}]]])

(defn LastGitHubCommit
  [last-timestamp]
  [:span (-> (ocall js/window "moment" last-timestamp)
             (ocall "fromNow"))])

(defn TableRow
  [idx {:keys [id github lastGitHubCommit name stoplight]}]
  [:tr {:key idx}
    [:td {:style {:text-align "center"}} [common/Stoplight stoplight]]
    [:td [:a {:href (str "#/students/" id)} name]]
    [:td [common/GitHubLink github]]
    [:td (when lastGitHubCommit
           (LastGitHubCommit lastGitHubCommit))]])

(defn StudentsTable
  []
  (let [sorted-students @(rf/subscribe [:sorted-and-filtered-students])]
    [:table.table.is-striped.is-hoverable.is-fullwidth
      [THead]
      [:tbody
        (map-indexed TableRow sorted-students)]]))

(def zero-commits-color "#ebedf0")
(def sml-commits-color "#c6e48b")
(def med-commits-color "#7bc96f")
(def lrg-commits-color "#239a3b")
(def xl-commits-color "#196127")

(defn- num-commits-color [num-commits]
  (cond
    (zero? num-commits) zero-commits-color
    (< num-commits 8) sml-commits-color
    (< num-commits 15) med-commits-color
    (< num-commits 22) lrg-commits-color
    :else xl-commits-color))

;; TODO: add hover-over for each cell here
(defn CommitGraphDay [{:keys [commit-count]}]
  [:div.cell.day
    {:style {:background-color (num-commits-color commit-count)}}])

(defn- random-commit-count []
  (let [x (rand-int 4)]
    (cond
      (= x 0) 0
      (= x 1) (rand-int 5)
      (= x 2) (rand-int 15)
      :else (rand-int 25))))

(defn- activity-count [activity-list day]
  (count
    (filter
      (fn [itm]
        (and (= "PushEvent" (:type itm))
             (string? (:created_at itm))
             (str/starts-with? (:created_at itm) day)))
      activity-list)))

;; TODO: use <svg> instead here?
(defn CommitGraph [github-activity]
  (let [activity-list (flatten (vals github-activity))]
    [:div
      [:h5.thin-header "Latest Commits"]
      [:div.commit-graph
        [:div.row.days-row
          [:div.week-count]
          [:div.cell]
          [:div.cell]
          [:div.cell.day-abbreviation "Tu"]
          [:div.cell]
          [:div.cell.day-abbreviation "Th"]
          [:div.cell]
          [:div.cell.day-abbreviation "Sa"]]
        [:div.row.week
          [:div.week-count "#1"]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-16")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-17")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-18")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-19")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-20")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-21")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-22")}]]
        [:div.row.week
          [:div.week-count "#2"]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-23")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-24")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-25")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-26")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-27")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-28")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-29")}]]
        [:div.row.week
          [:div.week-count "#3"]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-06-30")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-01")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-02")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-03")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-04")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-05")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-06")}]]
        [:div.row.week
          [:div.week-count "#4"]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-07")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-08")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-09")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-10")}]
          [CommitGraphDay {:commit-count (activity-count activity-list "2019-07-11")}]
          [:div.cell]
          [:div.cell]]]]))

(def placeholder-96px "https://bulma.io/images/placeholders/96x96.png")

(defn TileTop [{:keys [avatar name github stoplight]}]
  [:div.media.tile-top
    [:div.media-left
      [:figure.image.is-48x48
        [:img {:src (if (string? avatar) avatar placeholder-96px)
               :alt (str "Avatar for " name)}]]]
    [:div.media-content
      [:p.title.is-4
        name
        [:span {:style {:display "inline-block"
                        :margin-left "6px"}}
          [common/Stoplight stoplight]]]
      (when (and github
                 (not (str/blank? github)))
        [:p.subtitle.is-6 [common/GitHubLink github]])]])

(defn SmallTag [idx {:keys [label status]}]
  [:span.tag
    {:class [(when (= status "good") "is-success")
             (when (= status "bad") "is-warning")
             (when (= status "really-bad") "is-danger")]
     :key idx}
    label])

(defn LastTouchpoint [{:keys [isOldEvent timeAgo recordedBy stoplight comment tags]}]
  [:div
    [:h5.thin-header "Last Touchpoint"]
    (when stoplight
      [:p.stoplight-status "Status: " [:strong (str/capitalize stoplight)]])
    (when (and comment (not (str/blank? comment)))
      [:blockquote.comment comment])
    (when-not (empty? tags)
      [:div.tags (map-indexed SmallTag tags)])
    ;; NOTE: this conditional will go away when we are not using mock data
    (when recordedBy
      [:p.recorded-by
        (if isOldEvent
          [:strong timeAgo]
          timeAgo)
        " by " recordedBy])])

(defn- click-add-touchpoint [student-id js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:touchpoint-modal/open student-id]))

(defn StudentTile [{:keys [avatar events id name github githubActivityResponse vents stoplight] :as student}]
  [:div.card.student-card
    [:div.card-content
      [TileTop student]
      [:div.columns
        [:div.column [LastTouchpoint (first events)]]
        [:div.column.is-narrow [CommitGraph githubActivityResponse]]]]
    [:footer.card-footer
      [:a.card-footer-item
        {:href "#"
         :on-click util/prevent-default}
        "Details"]
      [:a.card-footer-item
        {:href "#"
         :on-click (partial click-add-touchpoint id)}
        "Add Touchpoint"]]])

(defn TileRow [row-idx students]
  [:div.columns {:key row-idx}
    (map-indexed
      (fn [cell-idx student]
        [:div.column {:key cell-idx}
          (when student
            [StudentTile student])])
      students)])

(def num-columns 4)

(defn StudentsTiles
  []
  (let [sorted-students @(rf/subscribe [:sorted-and-filtered-students])
        student-chunks (partition num-columns num-columns (repeat false) sorted-students)]
    [:div
      (map-indexed TileRow student-chunks)]))

;; TODO: make this a toggle component instead of buttons
(defn ToggleViewButtons []
  [:div.field.is-grouped
    [:p.control
      [:button.button {:on-click #(rf/dispatch [:students-page/set-view-type "TABLE_VIEW"])} "Table View"]]
    [:p.control
      [:button.button {:on-click #(rf/dispatch [:students-page/set-view-type "TILES_VIEW"])} "Tiles View"]]])

(defn SearchBar []
  (let [search-txt @(rf/subscribe [:students-page/search-txt])]
    [:div
      [:input.input.is-medium
        {:on-change #(rf/dispatch [:students-page/set-search-txt (oget % "currentTarget.value")])
         :placeholder (str "Search students " ellipsis)
         :value search-txt}]]))

(defn FilterControls []
  [:div.columns
    [:div.column.is-two-thirds [ToggleViewButtons]]
    [:div.column.is-one-third [SearchBar]]])

(defn StudentsPage []
  (let [view-type @(rf/subscribe [:students-page/view-type])]
    [:section.section
      [common/ClassHeader]
      [common/PrimaryNav]
      [:div.content
        [FilterControls]
        (case view-type
          "TABLE_VIEW" [StudentsTable]
          "TILES_VIEW" [StudentsTiles]
          (timbre/error "Invalid Students Page view-type"))]]))
