(ns headmaster.ui.html.assignments-page
  (:require
    [headmaster.ui.html.layout :refer [ClassPageWrapper]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

; (defn AssignmentsBody
;   [app-state]
;   [:div.content
;     [:h2 "Assignments page:"]
;     [:ul
;       [:li "ability to quickly search all assignments and submissions"]
;       [:li "separate tab for assignments and submissions?"]]])
;
; (defn AssignmentsPage
;   [app-state]
;   (ClassPageWrapper AssignmentsBody app-state))
