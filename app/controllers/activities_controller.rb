class ActivitiesController < ApplicationController
  before_action :set_user
  before_action :set_activity, only: [:show, :edit, :update, :destroy]
  before_action :user_permissions

  # GET /user/:user_id/activities
  def index
    @activities = @user.activities
  end

  # GET /user/:user_id/activities/:id
  def show
  end

  # GET /user/:user_id/activities/new
  def new
    @activity = @user.activities.new
  end

  # GET /user/:user_id/activities/:id/edit
  def edit
  end

  # POST /user/:user_id/activities
  def create
    @activity = @user.activities.new(activity_params)

    if @activity.save
      flash[:notice] = 'A new activity was successfully created.'
      redirect_to user_activity_path(@user, @activity)
    else
      render :new
    end
  end

  # PATCH/PUT /user/:user_id/activities/:id
  def update
    if @activity.update(activity_params)
      flash[:success] = 'The activity was successfully updated.'
      redirect_to user_activity_path(@user, @activity)
    else
      render :edit
    end
  end

  # DELETE /user/:user_id/activities/:id
  def destroy
    @activity.destroy
    flash[:success] = 'The activity was successfully deleted.'
    redirect_to user_activities_path(@user)
  end

  private
    def set_activity
      @activity = Activity.find(params[:id])
    end

    def set_user
      @user = User.find(params[:user_id])
    end

    def activity_params
      params.require(:activity).permit(:name, :color)
    end
end
