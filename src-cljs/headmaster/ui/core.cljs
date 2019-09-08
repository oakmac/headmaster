(ns headmaster.ui.core
  (:require
    [cljsjs.moment]
    [goog.functions :as gfunctions]
    [headmaster.ui.ajax :as ajax]
    [headmaster.ui.components.touchpoint-modal :refer [TouchpointModal]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    [headmaster.ui.events]
    [headmaster.ui.pages.manage-students :refer [ManageStudentsPage]]
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
      ;; TODO: make this a multi-method
      (case page-id
        "STUDENTS_PAGE" [StudentsPage]
        "MANAGE_STUDENTS_PAGE" [ManageStudentsPage]

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

(defn- fetch-mock-cohort-data []
  (rf/dispatch [:fetch-cohort-data]))

(def one-second-ms 1000)
(def ten-seconds-ms (* 10 one-second-ms))

(defn polling-rate-ms []
  (if (in-mock-mode?)
    one-second-ms
    ten-seconds-ms))

(defn- start-polling-cohort-data []
  (fetch-mock-cohort-data)
  (js/setInterval fetch-mock-cohort-data (polling-rate-ms)))

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
         (start-polling-cohort-data))))))
