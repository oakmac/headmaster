(ns headmaster.ui.routing
  "TODO: use bide here"
  (:require
    [clojure.string :as str]
    [goog.functions :as gfunctions]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall oget oset!]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

(def default-route "/students")

;; TODO: regex here instead
;; or use a proper routing library
(defn- student-page? [route]
  (str/includes? route "/students/student-"))

(def ^:private pages
  {"/students" "STUDENTS_PAGE"
   "/manage-students" "MANAGE_STUDENTS_PAGE"})

(defn- on-hash-change [_js-evt]
  (let [new-route (util/current-hash)
        new-page (get pages new-route)
        student-id (str/replace new-route "/students/" "")]
    (cond
      (student-page? new-route)
      (rf/dispatch [:student-page/init student-id])

      (= new-page "MANAGE_STUDENTS_PAGE")
      (rf/dispatch [:manage-students-page/init])

      (= new-page "STUDENTS_PAGE")
      (rf/dispatch [:students-page/init])

      :else
      (util/set-hash! default-route))))

(def init!
  (gfunctions/once
    (fn []
      (timbre/info "Initializing Routing")
      (oset! js/window "onhashchange" on-hash-change)
      (on-hash-change nil))))
