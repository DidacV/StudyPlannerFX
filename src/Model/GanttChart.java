package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Gantt chart representation for a module. 
 * Created by Dinara Adilova on 14/05/2017.
 */
public class GanttChart<X,Y> extends XYChart<X,Y> {
    private double frameHeight = 10;

    /** Helper class holds metadata about
     *  a data point(length of time and stylesheet)
     *
     */
    public static class MetaData {
        public double length;
        public String style;

        public MetaData(double length, String style) {
            //super();
            this.length = length;
            this.style = style;
        }

        public double getLength() { return length; }
        public String getStyle() { return style; }

        public void setLength(long length) { this.length = length; }
        public void setStyle(String style) { this.style = style; }
    }

    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param X  X Axis for this XY chart
     * @param Y Y Axis for this XY chart
     */
    public GanttChart(Axis<X> X, Axis<Y> Y) {
        this(X, Y, FXCollections.observableArrayList());
    }
    public GanttChart(Axis<X>X, Axis<Y> Y, ObservableList<Series<X,Y>> data) {
        super(X,Y);
        if (!(X instanceof NumberAxis && Y instanceof CategoryAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, X and Y should not both be NumberAxis");
        }
        setData(data);

    }
    public double getFrameHeight(){ return this.frameHeight; }
    public void setFrameHeight(double frameHeight){ this.frameHeight = frameHeight;}

    // Helper functions to retrieve metadata from data items
    private static String getStyleClass(Object obj) { return ((MetaData)obj).getStyle(); }
    private static double getLength(Object obj) { return ((MetaData)obj).getLength(); }
    // Overrides--------------------------------------------------------------------------------------------------------

    /** Function used to add
     *  a data item to the graph
     *
     * @param series
     * @param dataIndex
     * @param dataItem
     */
    @Override
    protected void dataItemAdded(Series<X,Y> series, int dataIndex, Data<X,Y> dataItem) {
        Node frame = createFrameHolder(dataItem);
        getPlotChildren().add(frame);
    }

    /** Function used to remove a data item
     *  of a series from the graph
     *
     * @param item
     * @param series
     */
    @Override
    protected void dataItemRemoved(Data item, Series series) {
        Node frame = item.getNode();
        getPlotChildren().remove(frame);
        removeDataItemFromDisplay(series,item);
    }

    /** function used to add a series
     *  of data points to the chart
     *
     * @param series
     * @param seriesIndex
     */
    @Override
    protected void seriesAdded(Series<X,Y> series, int seriesIndex) {
        for(int c=0; c< series.getData().size();c++){
            Data<X,Y> xyData = series.getData().get(c);
            Node frameHolder = createFrameHolder(xyData);
            getPlotChildren().add(frameHolder);
        }
    }

    /** Function updates axis range based on current data
     *
     */
    @Override
    protected void updateAxisRange(){
        Axis<X> xAxis = getXAxis();
        Axis<Y> yAxis = getYAxis();
        ArrayList<X> xList = null;
        ArrayList<Y> yList = null;
        if(xAxis.isAutoRanging()) { xList = new ArrayList<>(); }
        if(yAxis.isAutoRanging()) { yList = new ArrayList<>(); }

        if(xAxis != null || yAxis != null) {
            for (Series<X, Y> series : getData()) {
                for (Data<X, Y> data : series.getData()) {
                    if(xList != null) {
                        xList.add(data.getXValue());
                        xList.add(xAxis.toRealValue(xAxis.toNumericValue(data.getXValue())
                                + getLength(data.getExtraValue())));
                    }
                    if(yList != null) {
                        yList.add(data.getYValue());
                    }
                }
            }
            if(xList != null) { xAxis.invalidateRange(xList); }
            if(yList != null)  { yAxis.invalidateRange(yList); }
        }

    }

    /** Function used to remove a
     *  series of data points from the graph
     *
     * @param series
     */
    @Override
    protected void seriesRemoved(Series<X,Y> series) {
        for(XYChart.Data<X,Y> xydata : series.getData() ){
            Node holder = xydata.getNode();
            getPlotChildren().remove(holder);
        }
        removeSeriesFromDisplay(series);
    }

    /** Function used to create
     *  a node placeholder for a dataItem
     *
     * @param dataItem
     * @return node
     */
    private Node createFrameHolder(Data<X,Y> dataItem){
        Node frameHolder = dataItem.getNode();
        if (frameHolder == null){
            frameHolder = new StackPane();
            dataItem.setNode(frameHolder);
        }
        frameHolder.getStyleClass().add(getStyleClass(dataItem.getExtraValue()));
        return frameHolder;
    }

    /** Function used to plot points onto the graph
     *
     */
    @Override
    protected void layoutPlotChildren() {
        for(int index=0; index < getData().size(); index++){
            Series<X,Y> series = getData().get(index);
            Iterator<Data<X,Y>> it = getDisplayedDataIterator(series);
            while(it.hasNext()){
                Data<X,Y> data = it.next();
                double y = getYAxis().getDisplayPosition(data.getYValue());
                double x = getXAxis().getDisplayPosition(data.getXValue());
                if( Double.isNaN(x) || Double.isNaN(y)){ // CHECK THIS AND CHANGE
                    continue;
                }
                Node frame = data.getNode();
                javafx.scene.shape.Rectangle rectangle;
                if (frame != null) {
                    if (frame instanceof StackPane) {
                        StackPane region = (StackPane)data.getNode();
                        if (region.getShape() == null) {
                            double width = getLength(data.getExtraValue());
                            rectangle = new javafx.scene.shape.Rectangle(width,getFrameHeight());
                        } else if( region.getShape() instanceof Rectangle) {
                            rectangle = (Rectangle)region.getShape();
                        } else  return;
                        rectangle.setWidth(getLength(data.getExtraValue()) * ((getXAxis() instanceof NumberAxis)
                                ? Math.abs(((NumberAxis)getXAxis()).getScale()) : 1));
                        rectangle.setHeight(getFrameHeight() * ((getYAxis() instanceof NumberAxis)
                                ? Math.abs(((NumberAxis)getYAxis()).getScale()) : 1));
                        y -= getFrameHeight() / 2;

                        region.setShape(null);
                        region.setShape(rectangle);
                        region.setScaleShape(false);
                        region.setCenterShape(false);
                        region.setCacheShape(false);

                        frame.setLayoutX(x);
                        frame.setLayoutY(y);
                    }

                }
            }

        }
    }

    @Override
    protected void dataItemChanged(Data item) { }
}
