(ns headmaster.ui.core
  (:require
    [cljsjs.moment]
    [goog.functions :as gfunctions]
    [headmaster.ui.ajax :as ajax]
    [headmaster.ui.components.touchpoint-modal :refer [TouchpointModal]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    ; [headmaster.ui.html.assignments-page :refer [AssignmentsPage]]
    ; [headmaster.ui.html.attendance-page :refer [AttendancePage]]
    ; [headmaster.ui.html.student-page :refer [StudentPage]]
    [headmaster.ui.events]
    [headmaster.ui.pages.dashboard :refer [DashboardPage]]
    [headmaster.ui.pages.students :refer [StudentsPage]]
    [headmaster.ui.routing :as routing]
    [headmaster.ui.subs]
    [headmaster.ui.util :as util]
    [oops.core :refer [oget ocall]]
    [re-frame.core :as rf]
    [reagent.core :as reagent]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Root Component

(defn App
  "The Root component."
  []
  (let [page-id @(rf/subscribe [:page-id])
        touchpoint-modal @(rf/subscribe [:touchpoint-modal])]
    [:div
      (case page-id
        "DASHBOARD_PAGE" [DashboardPage]
        "STUDENTS_PAGE" [StudentsPage]
        ; "STUDENT_PAGE" [StudentPage]
        ; "ATTENDANCE_PAGE" [AttendancePage]
        ; "ASSIGNMENTS_PAGE" [AssignmentsPage]

        ;; FIXME: better error here
        [:div "<h1>No Page Found</h1>"])
      (when touchpoint-modal
        [TouchpointModal])]))

;; -----------------------------------------------------------------------------
;; Development Functions

(defn re-render-everything
  "Forces a Reagent re-render of all components.
   NOTE: this function is called after every shadow-cljs hot module reload"
  []
  (reagent/force-update-all))

(defn- fetch-mock-class-data []
  (ajax/fetch-class "class1"
    (fn [class-data]
      (rf/dispatch [:update-class class-data]))
    (fn []
      (timbre/error "Failed to fetch mock class data."))))

(defn- start-polling-classroom-data []
  (fetch-mock-class-data)
  (js/setInterval fetch-mock-class-data 1000))

;; -----------------------------------------------------------------------------
;; Init / Render Loop

(def app-container-el (util/by-id "appContainer"))

(def start-rendering!
  (gfunctions/once
    (fn []
      (timbre/info "Begin rendering")
      (reagent/render [App] app-container-el))))

(def init!
  "Global application init."
  (gfunctions/once
    (fn []
      (timbre/info "Initializing Headmaster UI 😎")
      (if-not app-container-el
       (timbre/fatal "<div id=appContainer> element not found")
       (do
         (rf/dispatch-sync [:init-db])
         (routing/init!)
         (start-rendering!)
         (start-polling-classroom-data))))))
