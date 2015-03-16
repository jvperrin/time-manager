class ActivityTime < ActiveRecord::Base
  # integer  "activity_id"
  # datetime "start"
  # float    "duration"
  # datetime "created_at",  null: false
  # datetime "updated_at",  null: false

  # Duration is measured in seconds lasted from the start time

  belongs_to :activity

  validates :start, :duration, :activity, presence: true
end
