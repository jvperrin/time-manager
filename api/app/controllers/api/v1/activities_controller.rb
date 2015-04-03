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

  # POST /set-current-activity
  def set_current
    past_activity = @user.current_activity
    if past_activity
      past_activity_time = past_activity.activity_times.last
      
      if past_activity_time
        duration = ((DateTime.now - past_activity_time.start.to_datetime) * 1.day).to_f
        past_activity_time.update_attribute(:duration, duration)
      end
    end
    
    current_activity = Activity.find(params[:id])
    @user.update_attribute(:current_activity_id, current_activity.id)
    new_activity_time = current_activity.activity_times.create(start: DateTime.now)
    render json: { success: "Changed to #{current_activity.name}" }, status: :ok
  end

  private
    def set_activity
      @activity = @user.activities.find(params[:id])
    end

    def activity_params
      params.require(:activity).permit(:name, :color)
    end
end
