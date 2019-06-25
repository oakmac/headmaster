(ns headmaster.ui.events
  (:require
    [headmaster.ui.ajax :as ajax]
    [headmaster.ui.db :as db]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;; -----------------------------------------------------------------------------
;; Effects

;; FIXME: almost certainly need to pass the class slug here
(rf/reg-fx
  :fetch-class-data
  (fn []
    (ajax/fetch-class "class1"
      (fn [class-data]
        (rf/dispatch [:update-class class-data]))
      (fn []
        (timbre/error "Failed to fetch class data.")))))

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
  :update-class
  (fn [db [_ class-data]]
    (assoc db :class class-data)))

(rf/reg-event-fx
  :fetch-class-data
  (fn [cofx _]
    {:fetch-class-data nil}))
