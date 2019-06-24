(ns headmaster.ui.html.student-page
  "Students page"
  (:require
    ; [headmaster.ui.actions.students-page :as actions]
    [headmaster.ui.html.common :refer [GitHubLink Stoplight SVGIcon]]
    [headmaster.ui.html.layout :refer [ClassPageWrapper]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

; (defn StudentBody
;   [app-state]
;   (let [student-id (get-in app-state [:student-page :id])
;         student (get-in app-state [:class :students student-id])]
;     [:div.content
;       [:h2.title.is-2 (:name student) " " (Stoplight (:stoplight student))]
;       [:p.subtitle.is-4 (GitHubLink (:github student))]
;       [:hr]
;       [:h4 "Student page:"]
;       [:ul
;         [:li "recent assignments"]
;         [:li "timeline of their activity"]]]))
;
; (defn StudentPage
;   [app-state]
;   (ClassPageWrapper StudentBody app-state))
