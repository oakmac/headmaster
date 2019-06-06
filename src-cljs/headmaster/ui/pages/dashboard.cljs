(ns headmaster.ui.pages.dashboard
  (:require
    [headmaster.ui.html.common :refer [ClassHeader PrimaryNav]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

;;------------------------------------------------------------------------------
;; Subscriptions

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :dashboard/init
  (fn [db _]
    (assoc db :page "DASHBOARD_PAGE")))

;;------------------------------------------------------------------------------
;; Views

(defn DashboardPage
  []
  [:section.section
    [ClassHeader]
    [PrimaryNav]
    [:div.content
      [:h2 "Dashboard page:"]
      [:ul
        [:li "Total number of students"]
        [:li "Topics for the next few classes"]
        [:li "Summary of Stoplight status (ie: 3 red, 2 yellow, 16 green)"]
        [:li "Number of days in; number of days left"]
        [:li "Total number of assignments submitted"]
        [:li "Date of next assignment submission due"]
        [:li "# of days until Final Project"]]]])
