class CreateActivities < ActiveRecord::Migration
  def change
    create_table :activities do |t|
      t.string :name
      t.references :user, index: true
      t.string :color

      t.timestamps null: false
    end
    add_foreign_key :activities, :users
  end
end
