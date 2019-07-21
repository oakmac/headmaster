(ns headmaster.backend.config
  (:require
    ["fs-plus" :as fs]
    [taoensso.timbre :as timbre]))

;; TODO: load config via file here

(def knexfile (js/require "./knexfile.js"))

; (timbre/info (js->clj knexfile))

(def default-config
  {:environment "development"
   :port 5005})

(def config
  (merge default-config
         {}))
