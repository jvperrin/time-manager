class User < ActiveRecord::Base
  # string   "email"
  # text     "password_digest"
  # datetime "created_at",      null: false
  # datetime "updated_at",      null: false
  # boolean  "admin",           default: false
  # string   "api_key"

  has_secure_password

  has_many :activities, dependent: :destroy

  belongs_to :current_activity, class_name: 'Activity'

  validates :email, presence: true

  before_create :generate_api_key

  def generate_api_key
    begin
      self.api_key = SecureRandom.hex
    end while self.class.exists?(api_key: api_key)
  end
end
