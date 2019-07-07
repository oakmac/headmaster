(ns headmaster.ui.components.common
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
       {:__html (str "<use xlink:href='/img/feather-icons-4.7.3.svg#" icon-id "' />")}}])

(defn NavLink
  [{:keys [active? href label]}]
  [:li {:class (when active? "is-active")}
    [:a {:href href} label]])

(defn CohortHeader []
  (let [title @(rf/subscribe [:cohort-title])
        subtitle @(rf/subscribe [:cohort-subtitle])]
    [:div {:style {:margin-bottom "20px"}}
      [:h1.title title]
      [:h2.subtitle subtitle]]))

(defn PrimaryNav []
  (let [current-page-id @(rf/subscribe [:page-id])]
    [:nav.tabs.is-boxed
      [:ul
        [NavLink {:active? (or (= current-page-id "STUDENTS_PAGE")
                               (= current-page-id "STUDENT_PAGE"))
                  :href "#/students"
                  :label "Students"}]
        [NavLink {:active? (= current-page-id "MANAGE_COHORT_PAGE")
                  :href "#/manage"
                  :label "Manage Cohort"}]]]))

(defn GitHubLink
  [username]
  [:a {:href (util/github-url username) :target "_blank"}
    [:span.icon.is-small.github-icon
      [:img {:src "/img/github.svg"}]]
    username])

(defn Stoplight
  [status {:keys [small?]}]
  [:span.icon
    {:class (when small? "is-small")}
    (if (= "red" status)
      [SVGIcon "alert-triangle" "stoplight-red"]
      [:span {:class (str "stoplight " (util/safe-lower-case status))}])])
