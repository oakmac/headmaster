(ns headmaster.ui.ajax
  (:require
    [ajax.core :refer [GET POST]]
    [headmaster.ui.config :refer [config in-mock-mode?]]
    [oops.core :refer [ocall]]
    [taoensso.timbre :as timbre]))

(defn- class-url [class-id]
  (if (in-mock-mode?)
    (str "data/demo-class1.json")
    "TODO: real URL here")) ; (str "/data/" class-id)))

(defn fetch-class [class-id success-fn error-fn]
  (GET (class-url class-id)
    {:error-handler error-fn
     :handler success-fn
     :keywords? true
     :response-format :json}))

(defn create-touchpoint [touchpoint success-fn error-fn]
  (if (in-mock-mode?)
    (js/setTimeout success-fn (+ 250 (rand-int 500)))
    (timbre/info "TODO: ajax/create-touchpoint write me!")))
