class CreateActivityTimes < ActiveRecord::Migration
  def change
    create_table :activity_times do |t|
      t.references :activity, index: true
      t.datetime :start
      t.float :duration

      t.timestamps null: false
    end
    add_foreign_key :activity_times, :activities
  end
end
