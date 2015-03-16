class Api::V1::SessionsController < Api::ApiController
  respond_to :json

  # POST /signin
  def create
    user = User.where(email: params[:email]).first

    if user && user.authenticate(params[:password])
      render json: user, except: [:password_digest, :admin], status: :created
    else
      render json: { error: "Invalid email/password combination" }, status: :unauthorized
    end
  end
end
