class Api::V1::UsersController < Api::ApiController
  before_action :authenticate

  respond_to :json

  # GET /user
  def show
    @user =
    render json: @user, except: [:password_digest, :admin], status: :ok
  end

  # PATCH/PUT /user
  def update
    if @user.update(user_params)
      render json: @user, except: [:password_digest, :admin], status: :ok, location: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  private

    def user_params
      params.require(:user).permit(:email, :password, :password_confirmation)
    end
end
