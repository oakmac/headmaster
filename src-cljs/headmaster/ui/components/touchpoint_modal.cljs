(ns headmaster.ui.components.touchpoint-modal
  (:require
    [clojure.string :as str]
    [headmaster.ui.ajax :as ajax]
    [headmaster.ui.constants :refer [ellipsis]]
    [headmaster.ui.html.common :as common]
    [headmaster.ui.util :as util]
    [oops.core :refer [ocall oget]]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Effects

(rf/reg-fx
  :save-new-touchpoint
  (fn [new-touchpoint]
    (ajax/create-touchpoint
      new-touchpoint
      (fn []
        ;; TODO: re-fetch latest class status here?
        (rf/dispatch [:touchpoint-modal/close]))
      (fn []
        (timbre/error "Failed to save new touchpoint!")))))

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

(rf/reg-sub
  :touchpoint-modal/error
  (fn [db]
    (get-in db [:touchpoint-modal :error])))

(rf/reg-sub
  :touchpoint-modal/saving?
  (fn [db]
    (get-in db [:touchpoint-modal :saving?])))

;;------------------------------------------------------------------------------
;; Events

(def empty-touchpoint
  {:comment ""
   :stoplight nil})

(rf/reg-event-db
  :touchpoint-modal/open
  (fn [db [_ student-id]]
    (assoc db :touchpoint-modal {:student-id student-id
                                 :touchpoint empty-touchpoint})))

(rf/reg-event-db
  :touchpoint-modal/close
  (fn [db _]
    (assoc db :touchpoint-modal nil)))

(rf/reg-event-db
  :touchpoint-modal/update
  (fn [db [_ m]]
    (update-in db [:touchpoint-modal :touchpoint] merge m)))

(defn- empty-touchpoint? [{:keys [comment stoplight]}]
  (and
    (not stoplight)
    (str/blank? comment)))

(def nothing-entered-error-msg
  "Please provide some information for this touchpoint.")

(rf/reg-event-fx
  :touchpoint-modal/save-touchpoint
  (fn [{:keys [db]} _]
    (let [new-touchpoint (get-in db [:touchpoint-modal :touchpoint])]
      (if (empty-touchpoint? new-touchpoint)
        {:db (assoc-in db [:touchpoint-modal :error] nothing-entered-error-msg)}
        {:db (assoc-in db [:touchpoint-modal :saving?] true)
         :save-new-touchpoint new-touchpoint}))))

;;------------------------------------------------------------------------------
;; Views

(defn- click-stoplight [new-status js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:touchpoint-modal/update {:stoplight new-status}]))

(defn StoplightStatusField []
  (let [new-touchpoint @(rf/subscribe [:touchpoint-modal/new-touchpoint])
        status (:stoplight new-touchpoint)]
   [:div.field
     [:label.label "Stoplight Status"]
     [:div.control
       [:div {:class ["tabs" "is-toggle"]}
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
               [:span "Red"]]]]]]]))

(defn- random-comment-placeholder []
  (rand-nth
    [(str "{shortName} really nailed the DOM exercise" ellipsis)
     (str "{shortName} was struggling with for loops" ellipsis)
     (str "{shortName} was really excited about learning AJAX" ellipsis)]))

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
        {:keys [comment]} @(rf/subscribe [:touchpoint-modal/new-touchpoint])
        error @(rf/subscribe [:touchpoint-modal/error])]
    [:div
      [StoplightStatusField]
      [CommentField]
      ;; TODO:
      ; [TagsField]]))
      ;; - record now vs previous time?
      (when error
        [:div.notification.is-warning error])]))

(defn- close-modal [js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:touchpoint-modal/close]))

(defn- submit-new-touchpoint [js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:touchpoint-modal/save-touchpoint]))

(defn- Footer []
  (let [saving? @(rf/subscribe [:touchpoint-modal/saving?])]
    [:footer.modal-card-foot
      [:button.button.is-success
        {:type "submit"}
        "Add Touchpoint"]
      [:button.button
        {:on-click close-modal}
        "Cancel"]]))

(defn SavingModal []
  [:div.modal.is-active
    [:div.modal-background]
    [:div.modal-content
      [:div.box
        [:h3.subtitle.is-3
          [:span.icon.is-medium
            [:i.fas.fa-cog.fa-spin.fa-sm]]
          (str " Saving " ellipsis)]]]])

(defn AddTouchpointModal []
  (let [{:keys [name] :as student} @(rf/subscribe [:touchpoint-modal/student])]
    [:div.modal.is-active
      [:form {:on-submit submit-new-touchpoint}
        [:div.modal-background]
        [:div.modal-card
          [:header.modal-card-head
            [:p.modal-card-title "Add Touchpoint for " name]
            [:button.delete {:on-click close-modal}]]
          [:section.modal-card-body
            [TouchpointInputs]]
          [Footer]]]]))

(defn TouchpointModal []
  (let [saving? @(rf/subscribe [:touchpoint-modal/saving?])]
    (if saving?
      [SavingModal]
      [AddTouchpointModal])))
