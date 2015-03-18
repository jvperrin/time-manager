class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :null_session

  include SessionsHelper

  @@permissions_error = "You cannot access that page!"

  def admin_permissions
    unless current_user.try(:admin?)
      flash[:error] = @@permissions_error
      redirect_to root_path
    end
  end

  def user_permissions
    unless current_user == @user || current_user.try(:admin?)
      flash[:error] = @@permissions_error
      redirect_to root_path
    end
  end
end
