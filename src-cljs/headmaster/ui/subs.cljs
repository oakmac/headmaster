(ns headmaster.ui.subs
  (:require
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;; -----------------------------------------------------------------------------
;; Subscriptions

(rf/reg-sub
  :page-id
  (fn [db _]
    (:page db)))

(rf/reg-sub
  :cohort
  (fn [db _]
    (:cohort db)))

(rf/reg-sub
  :cohort-title
  (fn [db _]
    (get-in db [:cohort :title])))

(rf/reg-sub
  :cohort-subtitle
  (fn [db _]
    (get-in db [:cohort :subtitle])))

(rf/reg-sub
  :student
  (fn [db [_ student-id]]
    (get-in db [:cohort :students (keyword student-id)])))
