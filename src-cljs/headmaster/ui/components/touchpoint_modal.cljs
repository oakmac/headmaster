(ns headmaster.ui.components.touchpoint-modal
  (:require
    [clojure.string :as str]
    [headmaster.ui.html.common :as common]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall oget]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Subscriptions

(rf/reg-sub
  :touchpoint-modal
  (fn [db _]
    (:touchpoint-modal db)))

(rf/reg-sub
  :touchpoint-modal/student
  (fn [db]
    (let [student-id (get-in db [:touchpoint-modal :student-id])]
      (get-in db [:class :students (keyword student-id)]))))

(rf/reg-sub
  :touchpoint-modal/new-touchpoint
  (fn [db]
    (get-in db [:touchpoint-modal :touchpoint])))

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :touchpoint-modal/open
  (fn [db [_ student-id]]
    (assoc db :touchpoint-modal {:student-id student-id})))

(rf/reg-event-db
  :touchpoint-modal/close
  (fn [db _]
    (assoc db :touchpoint-modal nil)))

(rf/reg-event-db
  :touchpoint-modal/update
  (fn [db [_ m]]
    (update-in db [:touchpoint-modal :touchpoint] merge m)))

;;------------------------------------------------------------------------------
;; Views

(defn- close-modal [_js-evt]
  (rf/dispatch [:touchpoint-modal/close]))

(defn- click-stoplight [new-status js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:touchpoint-modal/update {:stoplight new-status}]))

(defn StoplightStatusField []
  (let [new-touchpoint @(rf/subscribe [:touchpoint-modal/new-touchpoint])
        status (:stoplight new-touchpoint)]
   [:div.field
     [:div.label "Stoplight Status"
       [:div.control
         [:div {:class "tabs is-toggle"}
           [:ul
             [:li {:class (when (= status "green") "is-active")}
               [:a {:href "#", :on-click (partial click-stoplight "green")}
                 [common/Stoplight "green"]
                 [:span "Green"]]]
             [:li {:class (when (= status "yellow") "is-active")}
               [:a {:href "#", :on-click (partial click-stoplight "yellow")}
                 [common/Stoplight "yellow"]
                 [:span "Yellow"]]]
             [:li {:class (when (= status "red") "is-active")}
               [:a {:href "#", :on-click (partial click-stoplight "red")}
                 [common/Stoplight "red"]
                 [:span "Red"]]]]]]]]))

(defn- random-comment-placeholder []
  (rand-nth
    ["{shortName} really nailed the DOM exercise …"
     "{shortName} was struggling with for loops …"
     "{shortName} was really excited about learning AJAX …"]))

(def comment-placeholder (random-comment-placeholder))

(defn CommentField []
  (let [{:keys [comment]} @(rf/subscribe [:touchpoint-modal/new-touchpoint])
        {:keys [shortName]} @(rf/subscribe [:touchpoint-modal/student])]
    [:div.field
      [:label.label "Comment"]
      [:div.control
        [:textarea.textarea
          {:on-change #(rf/dispatch [:touchpoint-modal/update {:comment (oget % "currentTarget.value")}])
           :placeholder (str/replace comment-placeholder "{shortName}" shortName)
           :value comment}]]]))

(defn CommonTags []
  [:div {:class "field is-grouped is-grouped-multiline"}
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Technology"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "CSS"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Flexbox"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Web Design"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Open Source"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Community"]
     [:a {:class "tag is-delete"}]]]
   [:div {:class "control"}
    [:div {:class "tags has-addons"}
     [:a {:class "tag is-link"} "Documentation"]
     [:a {:class "tag is-delete"}]]]])

(defn TagsField []
  (let [{:keys [comment]} @(rf/subscribe [:touchpoint-modal/new-touchpoint])
        {:keys [shortName]} @(rf/subscribe [:touchpoint-modal/student])]
    [:div.field
      [:label.label "Tags"]
      [:div.control
        [CommonTags]]]))

(defn TouchpointInputs []
  (let [{:keys [name] :as student} @(rf/subscribe [:touchpoint-modal/student])
        {:keys [comment]} @(rf/subscribe [:touchpoint-modal/new-touchpoint])]
    [:div
      [StoplightStatusField]
      [CommentField]]))
      ;; TODO:
      ; [TagsField]]))
      ;; - record now vs previous time?

(defn TouchpointModal []
  (let [touchpoint-modal @(rf/subscribe [:touchpoint-modal])
        {:keys [name] :as student} @(rf/subscribe [:touchpoint-modal/student])]
    [:div.modal.is-active
      [:div.modal-background]
      [:div.modal-card
        [:header.modal-card-head
          [:p.modal-card-title "Add Touchpoint for " name]
          [:button.delete {:on-click close-modal}]]
        [:section.modal-card-body
          [TouchpointInputs]]
        [:footer.modal-card-foot
          [:button.button.is-success "Add Touchpoint"]
          [:button.button {:on-click close-modal}"Cancel"]]]]))
