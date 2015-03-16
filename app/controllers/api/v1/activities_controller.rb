class Api::V1::ActivitiesController < Api::ApiController
  before_action :authenticate
  before_action :set_activity, only: [:show, :update, :destroy]

  respond_to :json

  # GET /activities
  def index
    render json: @user.activities, except: [:user_id]
  end

  # GET /activities/:id
  def show
    render json: @activity, except: [:user_id]
  end

  # POST /activities
  def create
    @activity = @user.activities.new(activity_params)

    if @activity.save
      render json: @activity, except: [:user_id], status: :created, location: api_v1_activity_path(@activity)
    else
      render json: @activity.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /activities/:id
  def update
    if @activity.update(activity_params)
      render json: @activity, except: [:user_id], status: :ok, location: api_v1_activity_path(@activity)
    else
      render json: @activity.errors, status: :unprocessable_entity
    end
  end

  # DELETE /activities/:id
  def destroy
    @activity.destroy
    render json: { success: "The activity was deleted" }, status: :ok
  end

  private
    def set_activity
      @activity = @user.activities.find(params[:id])
    end

    def activity_params
      params.require(:activity).permit(:name, :color)
    end
end
