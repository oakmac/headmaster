(ns headmaster.ui.events
  (:require
    [headmaster.ui.db :as db]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

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
