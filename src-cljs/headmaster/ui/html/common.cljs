(ns headmaster.ui.html.common
  (:require
    [clojure.string :as str]
    [headmaster.ui.util :as util]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

(defn SVGIcon
  [icon-id svg-class]
  [:svg
    {:class (str "feather-icon " svg-class)
     :dangerouslySetInnerHTML
       {:__html (str "<use xlink:href='img/feather-icons-4.7.3.svg#" icon-id "' />")}}])

(defn NavLink
  [{:keys [active? href label]}]
  [:li {:class (when active? "is-active")}
    [:a {:href href} label]])

(defn ClassHeader []
  (let [class-title @(rf/subscribe [:class-title])
        class-subtitle @(rf/subscribe [:class-subtitle])]
    [:div {:style {:margin-bottom "20px"}}
      [:h1.title class-title]
      [:h2.subtitle class-subtitle]]))

(defn PrimaryNav []
  (let [current-page-id @(rf/subscribe [:page-id])]
    [:nav.tabs.is-boxed
      [:ul
        [NavLink {:active? (= current-page-id "DASHBOARD_PAGE")
                  :href "#/dashboard"
                  :label "Dashboard"}]
        [NavLink {:active? (or (= current-page-id "STUDENTS_PAGE")
                               (= current-page-id "STUDENT_PAGE"))
                  :href "#/students"
                  :label "Students"}]]]))
        ; [NavLink {:active? (= current-page-id "SCHEDULE_PAGE")
        ;           :href "#/schedule"
        ;           :label "Schedule"}]
        ; [NavLink {:active? (= current-page-id "ATTENDANCE_PAGE")
        ;           :href "#/attendance"
        ;           :label "Attendance"}]
        ; [NavLink {:active? (= current-page-id "ASSIGNMENTS_PAGE")
        ;           :href "#/assignments"
        ;           :label "Assignments"}]]]))

(defn GitHubLink
  [username]
  [:a {:href (util/github-url username) :target "_blank"}
    [:span.icon.is-small.github-icon
      [:img {:src "img/github.svg"}]]
    username])

(defn Stoplight
  [status]
  [:span.icon
    (if (= "red" status)
      [SVGIcon "alert-triangle" "stoplight-red"]
      [:span {:class (str "stoplight " (str/lower-case status))}])])
