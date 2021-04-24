package top.keiskeiframework.system.dto.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/14 16:27
 */
@NoArgsConstructor
@Data
public class DashboardDTO implements Serializable {

    /**
     * title : {"text":"雨量流量关系图","subtext":"数据来自西安兰特水电测控技术有限公司","left":"center","align":"right"}
     * grid : {"bottom":80}
     * toolbox : {"feature":{"dataZoom":{"yAxisIndex":"none"},"restore":{},"saveAsImage":{}}}
     * tooltip : {"trigger":"axis","axisPointer":{"type":"cross","animation":false,"label":{"backgroundColor":"#505765"}}}
     * legend : {"data":["流量","降雨量"],"left":10}
     * dataZoom : [{"show":true,"realtime":true,"start":65,"end":85},{"type":"inside","realtime":true,"start":65,"end":85}]
     * xAxis : [{"type":"category","boundaryGap":false,"axisLine":{"onZero":false},"data":[]}]
     * yAxis : [{"name":"流量(m^3/s)","type":"value","max":500},{"name":"降雨量(mm)","nameLocation":"start","max":5,"type":"value","inverse":true}]
     * series : [{"name":"流量","type":"line","areaStyle":{},"lineStyle":{"width":1},"emphasis":{"focus":"series"},"markArea":{"silent":true,"itemStyle":{"opacity":0.3},"data":[[{"xAxis":"2009/9/12\n7:00"},{"xAxis":"2009/9/22\n7:00"}]]},"data":[]},{"name":"降雨量","type":"line","yAxisIndex":1,"areaStyle":{},"lineStyle":{"width":1},"emphasis":{"focus":"series"},"markArea":{"silent":true,"itemStyle":{"opacity":0.3},"data":[[{"xAxis":"2009/9/10\n7:00"},{"xAxis":"2009/9/20\n7:00"}]]},"data":[]}]
     */

    private TitleDTO title;
    private GridDTO grid;
    private ToolboxDTO toolbox;
    private TooltipDTO tooltip;
    private LegendDTO legend;
    private List<DataZoomDTOX> dataZoom;
    private List<XAxisDTO> xAxis;
    private List<YAxisDTO> yAxis;
    private List<SeriesDTO> series;

    @NoArgsConstructor
    @Data
    public static class TitleDTO implements Serializable {
        /**
         * text : 雨量流量关系图
         * subtext : 数据来自西安兰特水电测控技术有限公司
         * left : center
         * align : right
         */

        private String text;
        private String subtext;
        private String left;
        private String align;
    }

    @NoArgsConstructor
    @Data
    public static class GridDTO implements Serializable {
        /**
         * bottom : 80
         */

        private Integer bottom;
    }

    @NoArgsConstructor
    @Data
    public static class ToolboxDTO implements Serializable {
        /**
         * feature : {"dataZoom":{"yAxisIndex":"none"},"restore":{},"saveAsImage":{}}
         */

        private FeatureDTO feature;

        @NoArgsConstructor
        @Data
        public static class FeatureDTO implements Serializable {
            /**
             * dataZoom : {"yAxisIndex":"none"}
             * restore : {}
             * saveAsImage : {}
             */

            private DataZoomDTO dataZoom;
            private RestoreDTO restore;
            private SaveAsImageDTO saveAsImage;

            @NoArgsConstructor
            @Data
            public static class DataZoomDTO implements Serializable {
            }

            @NoArgsConstructor
            @Data
            public static class RestoreDTO implements Serializable {
            }

            @NoArgsConstructor
            @Data
            public static class SaveAsImageDTO implements Serializable {
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class TooltipDTO implements Serializable {
        /**
         * trigger : axis
         * axisPointer : {"type":"cross","animation":false,"label":{"backgroundColor":"#505765"}}
         */

        private String trigger;
        private AxisPointerDTO axisPointer;

        @NoArgsConstructor
        @Data
        public static class AxisPointerDTO implements Serializable {
            /**
             * type : cross
             * animation : false
             * label : {"backgroundColor":"#505765"}
             */

            private String type;
            private Boolean animation;
            private LabelDTO label;

            @NoArgsConstructor
            @Data
            public static class LabelDTO implements Serializable {
                /**
                 * backgroundColor : #505765
                 */

                private String backgroundColor;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class LegendDTO implements Serializable {
        /**
         * data : ["流量","降雨量"]
         * left : 10
         */

        private Integer left;
        private List<String> data;
    }

    @NoArgsConstructor
    @Data
    public static class DataZoomDTOX implements Serializable {
        /**
         * show : true
         * realtime : true
         * start : 65
         * end : 85
         * type : inside
         */

        private Boolean show;
        private Boolean realtime;
        private Integer start;
        private Integer end;
        private String type;
    }

    @NoArgsConstructor
    @Data
    public static class XAxisDTO implements Serializable {
        /**
         * type : category
         * boundaryGap : false
         * axisLine : {"onZero":false}
         * data : []
         */

        private String type;
        private Boolean boundaryGap;
        private AxisLineDTO axisLine;
        private List<?> data;

        @NoArgsConstructor
        @Data
        public static class AxisLineDTO implements Serializable {
            /**
             * onZero : false
             */

            private Boolean onZero;
        }
    }

    @NoArgsConstructor
    @Data
    public static class YAxisDTO implements Serializable {
        /**
         * name : 流量(m^3/s)
         * type : value
         * max : 500
         * nameLocation : start
         * inverse : true
         */

        private String name;
        private String type;
        private Integer max;
        private String nameLocation;
        private Boolean inverse;
    }

    @NoArgsConstructor
    @Data
    public static class SeriesDTO implements Serializable {
        /**
         * name : 流量
         * type : line
         * areaStyle : {}
         * lineStyle : {"width":1}
         * emphasis : {"focus":"series"}
         * markArea : {"silent":true,"itemStyle":{"opacity":0.3},"data":[[{"xAxis":"2009/9/12\n7:00"},{"xAxis":"2009/9/22\n7:00"}]]}
         * data : []
         * yAxisIndex : 1
         */

        private String name;
        private String type;
        private AreaStyleDTO areaStyle;
        private LineStyleDTO lineStyle;
        private EmphasisDTO emphasis;
        private MarkAreaDTO markArea;
        private Integer yAxisIndex;
        private List<?> data;

        @NoArgsConstructor
        @Data
        public static class AreaStyleDTO implements Serializable {
        }

        @NoArgsConstructor
        @Data
        public static class LineStyleDTO implements Serializable {
            /**
             * width : 1
             */

            private Integer width;
        }

        @NoArgsConstructor
        @Data
        public static class EmphasisDTO implements Serializable {
            /**
             * focus : series
             */

            private String focus;
        }

        @NoArgsConstructor
        @Data
        public static class MarkAreaDTO implements Serializable {
            /**
             * silent : true
             * itemStyle : {"opacity":0.3}
             * data : [[{"xAxis":"2009/9/12\n7:00"},{"xAxis":"2009/9/22\n7:00"}]]
             */

            private Boolean silent;
            private ItemStyleDTO itemStyle;
            private List<List<DataDTO>> data;

            @NoArgsConstructor
            @Data
            public static class ItemStyleDTO implements Serializable {
                /**
                 * opacity : 0.3
                 */

                private Double opacity;
            }

            @NoArgsConstructor
            @Data
            public static class DataDTO implements Serializable {
                /**
                 * xAxis : 2009/9/12
                 7:00
                 */

                private String xAxis;
            }
        }
    }
}
