class Activity < ActiveRecord::Base
  # string   "name"
  # integer  "user_id"
  # string   "color"
  # datetime "created_at", null: false
  # datetime "updated_at", null: false

  belongs_to :user

  has_many :activity_times, dependent: :destroy

  validates :name, :color, :user, presence: true
  validates :name,  uniqueness: { scope: :user }
  validates :color, format: { with: /\A#[a-f\d]{6}\Z/i }

  before_validation :generate_color

  private

  def generate_color
    self.color = "#ffffff" unless color.present?
  end
end
