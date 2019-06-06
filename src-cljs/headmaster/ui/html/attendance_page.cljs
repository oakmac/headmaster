(ns headmaster.ui.html.attendance-page
  (:require
    [headmaster.ui.html.layout :refer [ClassPageWrapper]]
    [headmaster.ui.util :refer [js-log log]]
    [re-frame.core :as rf]))

; (defn AttendanceBody
;   [app-state]
;   [:div.content
;     [:h2 "Attendance page:"]
;     [:ul
;       [:li "take attendance primary action button"]
;       [:li "edit student attendance"]]])
;
; (defn AttendancePage
;   [app-state]
;   (ClassPageWrapper AttendanceBody app-state))
