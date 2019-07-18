(ns headmaster.backend.core
  (:require
    ; ["web-animations-js" :as js-web-animations]
    ["express" :as express]
    [goog.functions :as gfunctions]
    [oops.core :refer [ocall]]
    [headmaster.backend.config :refer [config]]
    [taoensso.timbre :as timbre]))

(def port 5005)

;; express.js application
(def app nil)

(def init-express-app!
  (gfunctions/once
    (fn []
      (set! app (express))
      (ocall app "listen" (:port config)
             #(timbre/info (str "express.js server started on port " (:port config)))))))

(def init!
  (gfunctions/once
    (fn []
      (timbre/info "Booting up Headmaster server now :)")
      ; (load-config!)
      ; (connect-to-db!)
      (init-express-app!))))
