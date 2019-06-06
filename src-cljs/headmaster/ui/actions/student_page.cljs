(ns headmaster.ui.actions.student-page
  "Actions for the Student Page"
  (:require
    [clojure.string :as str]
    [headmaster.ui.util :as util]))


; ;; TODO: make this more robust
; (defn- get-student-id-from-url []
;   (re-find #"student-.+$" (util/current-hash)))
;
;
; (defn- initial-page-state []
;   {})
;
;
; (defn init! []
;   (let [student-id (keyword (get-student-id-from-url))
;         student (get-in @*app-state [:class :students student-id])]
;     (swap! *app-state assoc :page "STUDENT_PAGE"
;                             :student-page (assoc student :id student-id))))
