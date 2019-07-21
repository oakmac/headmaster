(ns headmaster.backend.data
  (:require
    [clojure.core.async :refer [chan put!]]
    [headmaster.backend.db :as db]
    [oops.core :refer [oget]]
    [taoensso.timbre :as timbre]))

;; -----------------------------------------------------------------------------
;; Cohort

(defn get-cohort [id-or-slug success-fn]
  (db/run-query :get-cohort {:id id-or-slug :slug id-or-slug}
    (fn [result]
      (success-fn (first result)))))

(defn go-get-cohort
  "Returns a core.async channel"
  [id-or-slug]
  (let [c (chan)]
    (get-cohort id-or-slug
      (fn [cohort]
        (if cohort
          (put! c cohort)
          (put! c false))))
    c))

;; -----------------------------------------------------------------------------
;; Students

;; TODO: write me

;; -----------------------------------------------------------------------------
;; User

;; TODO: write me
