(ns headmaster.ui.pages.students
  "Students page"
  (:require
    [clojure.string :as str]
    [headmaster.ui.html.common :as common]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall]]
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
  (compare (str/lower-case a)
           (str/lower-case b)))

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
  :students-vec
  :<- [:class]
  (fn [class _]
    (mapv
      (fn [[id student]]
        (assoc student :id (name id)))
      (:students class))))

(rf/reg-sub
  :sorted-students
  :<- [:students-vec]
  :<- [:students-page/sort-col]
  :<- [:students-page/sort-dir]
  (fn [[students-vec sort-col sort-dir] _]
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
  (let [sorted-students @(rf/subscribe [:sorted-students])]
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

;; TODO: use <svg> instead here?
(defn CommitGraph []
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
        [:div.week-count "#8"]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]]
      [:div.row.week
        [:div.week-count "#9"]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]]
      [:div.row.week
        [:div.week-count "#11"]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]]
      [:div.row.week
        [:div.week-count "#12"]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [CommitGraphDay {:commit-count (random-commit-count)}]
        [:div.cell]
        [:div.cell]]]])

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
      [:p.subtitle.is-6 [common/GitHubLink github]]]])

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
    (when (string? comment)
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

(defn StudentTile [{:keys [avatar id name github events stoplight] :as student}]
  [:div.card.student-card
    [:div.card-content
      [TileTop student]
      [:div.columns
        [:div.column [LastTouchpoint (first events)]]
        [:div.column.is-narrow [CommitGraph student]]]]
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
  (let [sorted-students @(rf/subscribe [:sorted-students])
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

(defn StudentsPage []
  (let [view-type @(rf/subscribe [:students-page/view-type])]
    [:section.section
      [common/ClassHeader]
      [common/PrimaryNav]
      [:div.content
        [ToggleViewButtons]
        (case view-type
          "TABLE_VIEW" [StudentsTable]
          "TILES_VIEW" [StudentsTiles]
          (timbre/error "Invalid Students Page view-type"))]]))
