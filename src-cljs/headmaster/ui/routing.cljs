(ns headmaster.ui.routing
  "TODO: use bide here"
  (:require
    [clojure.string :as str]
    [goog.functions :as gfunctions]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall oget oset!]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

(def default-route "/dashboard")

;; TODO: regex here instead
;; or use a proper routing library
(defn- student-page? [route]
  (str/includes? route "/students/student-"))

(def ^:private pages
  {"/dashboard" "DASHBOARD_PAGE"
   "/students" "STUDENTS_PAGE"
   "/schedule" "SCHEDULE_PAGE"
   "/attendance" "ATTENDANCE_PAGE"
   "/assignments" "ASSIGNMENTS_PAGE"})

(defn- on-hash-change [_js-evt]
  (let [new-route (util/current-hash)
        new-page (get pages new-route)
        student-id (str/replace new-route "/students/" "")]
    (cond
      (student-page? new-route)
      (rf/dispatch [:student-page/init student-id])

      (= new-page "DASHBOARD_PAGE")
      (rf/dispatch [:dashboard/init])

      (= new-page "STUDENTS_PAGE")
      (rf/dispatch [:students-page/init])

      (= new-page "ATTENDANCE_PAGE")
      (rf/dispatch [:attendance-page/init])

      (= new-page "ASSIGNMENTS_PAGE")
      (rf/dispatch [:assignments-page/init])

      :else
      (util/set-hash! default-route))))

(def init!
  (gfunctions/once
    (fn []
      (timbre/info "Initializing Routing")
      (oset! js/window "onhashchange" on-hash-change)
      (on-hash-change nil))))
