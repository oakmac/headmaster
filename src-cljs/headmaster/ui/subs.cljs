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
  :class-title
  (fn [db _]
    (get-in db [:class :title])))

(rf/reg-sub
  :class-subtitle
  (fn [db _]
    (get-in db [:class :subtitle])))

(rf/reg-sub
  :class
  (fn [db _]
    (:class db)))

(rf/reg-sub
  :student
  (fn [db [_ student-id]]
    (get-in db [:class :students (keyword student-id)])))
