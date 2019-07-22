(ns headmaster.backend.config
  (:require
    ["fs-plus" :as fs]
    [taoensso.timbre :as timbre]))

(def default-config
  {:environment "development"
   :port 5005})

(def config-file-values
  (try
    (-> (.readFileSync fs "./server.config.json" "utf8")
        js/JSON.parse
        (js->clj :keywordize-keys true))
    (catch js/Error e nil)))

(when-not (map? config-file-values)
  (timbre/warn "Could not read ./server.config.json. Using default config."))

(def config
  (merge default-config config-file-values))
