(ns headmaster.ui.ajax
  (:require
    [ajax.core :refer [GET POST]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    [oops.core :refer [ocall]]))

(defn- class-url [class-id]
  (if (in-mock-mode?)
    (str "data/class1.json")
    "TODO: real URL here")) ; (str "/data/" class-id)))

(defn fetch-class [class-id success-fn error-fn]
  (GET (class-url class-id)
    {:error-handler error-fn
     :handler success-fn
     :keywords? true
     :response-format :json}))
