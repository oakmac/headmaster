(ns headmaster.ui.util
  (:require
    [clojure.string :as str]
    [oops.core :refer [ocall oget oset!]]))

(defn js-log
  "Log a JavaScript thing."
  [js-thing]
  (js/console.log js-thing))

(defn log
  "Log a Clojure thing."
  [clj-thing]
  (js-log (pr-str clj-thing)))

(defn by-id
  [id]
  (js/document.getElementById id))

(defn current-url []
  (oget js/window "location.href"))

(defn current-hash []
  (-> (current-url)
      (str/replace #"^.+#" "")))

(defn set-hash! [route]
  (oset! js/window "location.hash" route))

(defn github-url [username]
  (str "https://github.com/" username))

(defn prevent-default [js-evt]
  (when (fn? (oget js-evt "preventDefault"))
    (ocall js-evt "preventDefault")))

;; TODO: be more defensive here?
(defn target-value [js-evt]
  (oget js-evt "currentTarget.value"))

(defn safe-lower-case [s]
  (str/lower-case (str s)))
