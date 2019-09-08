(ns headmaster.backend.html
  (:require-macros
    [hiccups.core :as hiccups :refer [html html5]])
  (:require
    [hiccups.runtime :as hiccupsrt]
    [taoensso.timbre :as timbre]))

(def bulma-cdn-url "https://unpkg.com/bulma@0.7.5/css/bulma.min.css")
(def bulma-integrity-hash "sha384-H5O3U+oUYwd/bFECZMaQ1XJlueV5e1gB4b7Xt0M06fPbgz48WH33qxUyQNPeZVou")

(defn- head [page-title]
  [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "x-ua-compatible" :content "ie=edge"}]
    [:title (str "Headmaster - " page-title)]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href bulma-cdn-url :integrity bulma-integrity-hash :crossorigin "anonymous"}]
    [:link {:rel "stylesheet" :href "/css/fontawesome-5.9.0.min.css"}]
    [:link {:rel "stylesheet" :href "/css/main.css"}]])

(defn homepage []
  (html5
    (head "Student Tracking Tool")
    [:body
      [:section.hero.is-primary.is-large.is-bold
        [:div.hero-body
          [:div.container
            [:h1.title "Headmaster"]
            [:h2.subtitle "Student Tracking Tool"]
            [:a.button.is-medium {:href "/login/github"}
              [:span.icon [:i.fab.fa-github]]
              [:span "Login with Github"]]]]]]))

(defn not-found-page []
  (html5
    (head "Page Not Found")
    [:body
      [:h1.title "Page not found"]]))

(defn login-page []
  (html5
    (head "Login")
    [:body
      [:h1.title "TODO: write login page"]]))
