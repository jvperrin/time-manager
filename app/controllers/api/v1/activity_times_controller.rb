class Api::V1::ActivityTimesController < Api::ApiController
  before_action :authenticate
  before_action :set_activity
  before_action :set_activity_time, only: [:show, :update, :destroy]

  respond_to :json

  # GET /activities/:activity_id/activity_times
  def index
    render json: @activity.activity_times, except: [:activity_id]
  end

  # GET /activities/:activity_id/activity_times/:id
  def show
    render json: @activity_time, except: [:activity_id]
  end

  # POST /activities/:activity_id/activity_times
  def create
    @activity_time = @activity.activity_times.new(activity_time_params)

    if @activity_time.save
      render json: @activity_time,
             except: [:activity_id],
             status: :created,
             location: api_v1_activity_activity_time_path(@activity, @activity_time)
    else
      render json: @activity_time.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /activities/:activity_id/activity_times/:id
  def update
    if @activity_time.update(activity_time_params)
      render json: @activity_time,
             except: [:activity_id],
             status: :ok,
             location: api_v1_activity_activity_time_path(@activity, @activity_time)
    else
      render json: @activity_time.errors, status: :unprocessable_entity
    end
  end

  # DELETE /activities/:activity_id/activity_times/:id
  def destroy
    @activity_time.destroy
    render json: { success: "The activity session was deleted" }, status: :ok
  end

  private
    def set_activity
      @activity = Activity.find(params[:activity_id])
    end

    def set_activity_time
      @activity_time = @activity.activity_times.find(params[:id])
    end

    def activity_time_params
      params.require(:activity_time).permit(:start, :duration)
    end
end
