(ns headmaster.ui.ajax
  (:require
    [ajax.core :refer [GET POST]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    [oops.core :refer [ocall]]
    [taoensso.timbre :as timbre]))

(defn- class-url [class-id]
  (if (in-mock-mode?)
    (str "data/demo-class1.json")
    (str "/api/dashboard")))

(defn fetch-class [class-id success-fn error-fn]
  (GET (class-url class-id)
    {:error-handler error-fn
     :handler success-fn
     :keywords? true
     :response-format :json}))

(defn invite-student [class-id success-fn error-fn]
  (if (in-mock-mode?)
    (js/setTimeout success-fn (+ 250 (rand-int 500)))
    (POST (str "/api/classroom/" class-id "/invitation")
          {:error-handler error-fn
           :format :json
           :handler success-fn
           :keywords? true
           :params {:class-id [class-id]}
           :response-format :json})))

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
