(ns headmaster.backend.config
  (:require
    ["fs-plus" :as fs]))

;; TODO: load config via file here

(def default-config
  {:port 5005})

(def config
  (merge default-config
         {}))
