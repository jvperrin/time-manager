class AddCurrentActivityToUsers < ActiveRecord::Migration
  def change
    add_column :users, :current_activity_id, :integer
  end
end
