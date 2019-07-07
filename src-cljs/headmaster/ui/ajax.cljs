(ns headmaster.ui.ajax
  (:require
    [ajax.core :refer [GET POST]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    [oops.core :refer [ocall]]
    [taoensso.timbre :as timbre]))

(defn- cohort-url [cohort-slug]
  (if (in-mock-mode?)
    (str "data/demo-cohort1.json")
    (str "/api/dashboard")))

(defn fetch-cohort [cohort-id success-fn error-fn]
  (GET (cohort-url cohort-id)
    {:error-handler error-fn
     :handler success-fn
     :keywords? true
     :response-format :json}))

(defn create-touchpoint [student-id touchpoint success-fn error-fn]
  (if (in-mock-mode?)
    (js/setTimeout success-fn (+ 250 (rand-int 500)))
    (POST (str "/api/students/" student-id "/touchpoints")
      {:error-handler error-fn
       :format :json
       :handler success-fn
       :keywords? true
       :params {:touchpoints [touchpoint]}
       :response-format :json})))
