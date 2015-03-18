class Api::ApiController < ActionController::Base
  protect_from_forgery with: :null_session

  # APIs do not need layouts
  layout false

  @@permissions_error = "You cannot access that page!"

  private

  def authenticate
    authenticate_or_request_with_http_token do |token, options|
      @user = User.where(api_key: token).first
    end
  end
end
