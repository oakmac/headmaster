(ns headmaster.ui.events
  (:require
    [headmaster.ui.ajax :as ajax]
    [headmaster.ui.db :as db]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;; -----------------------------------------------------------------------------
;; Effects

;; FIXME: almost certainly need to pass the cohort slug here
(rf/reg-fx
  :fetch-cohort-data
  (fn []
    (ajax/fetch-cohort "cohort1"
      (fn [cohort-data]
        (rf/dispatch [:update-cohort cohort-data]))
      (fn []
        (timbre/error "Failed to fetch cohort data.")))))

;; -----------------------------------------------------------------------------
;; Events

;; :init event sets the initial app-db state
;; NOTE: this event is called synchronously
(rf/reg-event-db
  :init-db
  (fn [_ _]
    (timbre/info "Initializing app-db")
    db/initial-app-db))

(rf/reg-event-db
  :update-cohort
  (fn [db [_ cohort-data]]
    (assoc db :cohort cohort-data)))

(rf/reg-event-fx
  :fetch-cohort-data
  (fn [cofx _]
    {:fetch-cohort-data nil}))
