(ns headmaster.ui.pages.students
  "Students page"
  (:require
    [clojure.string :as str]
    [headmaster.ui.html.common :as common]
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

(defn StudentsPage []
  [:section.section
    [common/ClassHeader]
    [common/PrimaryNav]
    [:div.content
      [StudentsTable]]])
