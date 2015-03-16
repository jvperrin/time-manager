module SessionsHelper

  attr_writer :current_user
  def current_user
    sign_out if session[:expires_at] && session[:expires_at] < DateTime.now
    @current_user ||= User.find_by_id(session[:user_id]) if session[:user_id]
  end

  def signed_in?
    current_user.present?
  end

  def sign_in(user)
    reset_session
    session[:user_id] = user.id
    session[:expires_at] = DateTime.now + 1.month
    self.current_user = user
  end

  def sign_out
    reset_session
    @current_user = nil
  end
end
