(ns headmaster.ui.pages.manage-students
  (:require
    [headmaster.ui.components.common :as common]
    [headmaster.ui.util :as util]
    [re-frame.core :as rf]
    [taoensso.timbre :as timbre]))

;;------------------------------------------------------------------------------
;; Subscriptions

(rf/reg-sub
  :manage-students-page/students
  (fn [db _]
    (get-in db [:manage-students-page :students])))

;; FIXME: sort this consistently
(rf/reg-sub
  :manage-students-page/students-vec
  :<- [:manage-students-page/students]
  (fn [students-map _]
    (reduce
      (fn [students-vec [student-id student]]
        (conj students-vec (assoc student :id student-id)))
      []
      students-map)))

;;------------------------------------------------------------------------------
;; Events

(rf/reg-event-db
  :manage-students-page/init
  (fn [db _]
    (let [current-students (get-in db [:cohort :students])]
      (assoc db :page "MANAGE_STUDENTS_PAGE"
                :manage-students-page {:students current-students}))))

(defn- empty-student [new-id]
  {:id new-id
   :name ""
   :shortName ""
   :github ""})

(rf/reg-event-db
  :manage-students-page/add-student-row
  (fn [db _]
    (let [new-student-id (str "Student-" (random-uuid))]
      (update-in db [:manage-students-page :students] assoc new-student-id (empty-student new-student-id)))))

(rf/reg-event-db
  :manage-students-page/update-student
  (fn [db [_ student-id new-student-info]]
    (update-in db [:manage-students-page :students student-id] merge new-student-info)))

(rf/reg-event-fx
  :manage-students-page/save-students
  (fn [{:keys [db]} _]
    ;; FIXME: show a loading state here
    ;; FIXME: send an AJAX request with the new students
    {:db db}))

;;------------------------------------------------------------------------------
;; Views

(defn TitleInput []
  (let [title ""]
    [:div.field
      [:label.label "Title"]
      [:div.control
        [:input.input {:type "text" :placeholder "Cohort Title" :value title}]]]))

(defn SubtitleInput []
  (let [subtitle ""]
    [:div.field
      [:label.label "Subtitle"]
      [:div.control
        [:input.input {:type "text" :placeholder "Cohort Subtitle" :value subtitle}]]]))

;; FIXME: add Bulma styling to this
(defn StudentRow [idx {:keys [id] :as student}]
  [:div {:key idx}
    ; [:pre (js/JSON.stringify (clj->js student))]
    [:input ; .input.is-small
      {:type "text"
       :value (:name student)
       :on-change (fn [js-evt]
                    (rf/dispatch [:manage-students-page/update-student id {:name (util/target-value js-evt)}]))}]
    [:input ; .input.is-small
      {:type "text"
       :value (:shortName student)
       :on-change (fn [js-evt]
                    (rf/dispatch [:manage-students-page/update-student id {:shortName (util/target-value js-evt)}]))}]
    [:input ; .input.is-small
      {:type "text"
       :value (:github student)
       :on-change (fn [js-evt]
                    (rf/dispatch [:manage-students-page/update-student id {:github (util/target-value js-evt)}]))}]])

(defn ActionBar []
  [:div.field.is-grouped
    [:div.control
      [:button.button.is-primary
        {:type "submit"}
        "Save"]]])

(defn submit-students-form [js-evt]
  (util/prevent-default js-evt)
  (timbre/info "submit students form")
  (rf/dispatch [:manage-students-page/save-students]))

(defn- click-add-student-btn [js-evt]
  (util/prevent-default js-evt)
  (rf/dispatch [:manage-students-page/add-student-row]))

(defn AddStudentBtn []
  [:button.button.is-secondary
    {:on-click click-add-student-btn
     :type "button"}
    "+ Add Student"])

(defn EditStudentsForm []
  (let [students @(rf/subscribe [:manage-students-page/students-vec])]
    [:form {:on-submit submit-students-form}
      (map-indexed StudentRow students)
      [AddStudentBtn]
      [ActionBar]]))

(defn ManageStudentsPage
  []
  [:section.section
    [common/CohortHeader]
    [common/PrimaryNav]
    [:div.content
      [:h2 "Manage Students"]
      [:hr]
      [EditStudentsForm]]])

      ; [:ul
      ;   [:li "Add / Edit / Remove student"]
      ;   [:li "Start date for the Github graph"]
      ;   [:li "Edit title / subtitle"]
      ;   [:li "Create invite link"]
      ;   [:li "Add / Remove authorized users"]]
      ; [:hr]
      ; [InfoForm]]])



; <div class="field">
;   <label class="label">Name</label>
;   <div class="control">
;     <input class="input" type="text" placeholder="Text input">
;   </div>
; </div>
;
; <div class="field">
;   <label class="label">Username</label>
;   <div class="control has-icons-left has-icons-right">
;     <input class="input is-success" type="text" placeholder="Text input" value="bulma">
;     <span class="icon is-small is-left">
;       <i class="fas fa-user"></i>
;     </span>
;     <span class="icon is-small is-right">
;       <i class="fas fa-check"></i>
;     </span>
;   </div>
;   <p class="help is-success">This username is available</p>
; </div>
;
; <div class="field">
;   <label class="label">Email</label>
;   <div class="control has-icons-left has-icons-right">
;     <input class="input is-danger" type="email" placeholder="Email input" value="hello@">
;     <span class="icon is-small is-left">
;       <i class="fas fa-envelope"></i>
;     </span>
;     <span class="icon is-small is-right">
;       <i class="fas fa-exclamation-triangle"></i>
;     </span>
;   </div>
;   <p class="help is-danger">This email is invalid</p>
; </div>
