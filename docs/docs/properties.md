## Properties

### ObjectNode
- visible `: Boolean`
- opacity `: Number`
- clip `: Node`
- style, css `: String`
- rotate `: Number`
- translateX `: Number`
- translateY `: Number`
- translateZ `: Number`
- scaleX `: Number`
- scaleY `: Number`
- scaleZ `: Number`
- layoutX `: Number`
- layoutY `: Number`
- blendMode `: Enum (SRC_OVER=0, SRC_ATOP=1, ADD=2, MULTIPLY=3, SCREEN=4, OVERLAY=5, DARKEN=6, LIGHTEN=7, COLOR_DODGE=8, COLOR_BURN=9, HARD_LIGHT=10, SOFT_LIGHT=11, DIFFERENCE=12, EXCLUSION=13, RED=14, GREEN=15, BLUE=16)`

### ShapeNode `: ObjectNode`
- smooth `: Boolean`
- fill `: Paint`
- stroke `: Paint`
- strokeType `: Enum (INSIDE=0, OUTSIDE=1, CENTERED=2)`
- strokeWidth `: Number`
- strokeDashOffset `: Number`
- strokeMiterLimit `: Number`
- strokeLineCap `: Enum (SQUARE=0, BUTT=1, ROUND=2)`
- strokeLineJoin `: Enum (MITER=0, BEVEL=1, ROUND=2)`

### RegionNode `: ObjectNode`
- cacheShape `: Boolean`
- centerShape `: Boolean`
- scaleShape `: Boolean`
- snapToPixel `: Boolean`
- minWidth `: Number`
- minHeight `: Number`
- prefWidth `: Number`
- prefHeight `: Number`
- maxWidth `: Number`
- maxHeight `: Number`

### ArcNode `: ShapeNode`
- cx, centerX `: Number`
- cy, centerY `: Number`
- radiusX `: Number`
- radiusY `: Number`
- angle, startAngle `: Number`
- length `: Number`
- type `: Enum (OPEN=0, CHORD=1, ROUND=2)`

### CircleNode `: ShapeNode`
- cx, centerX `: Number`
- cy, centerY `: Number`
- radius `: Number`

### EllipseNode `: ShapeNode`
- cx, centerX `: Number`
- cy, centerY `: Number`
- radiusX `: Number`
- radiusY `: Number`

### GroupNode `: ObjectNode`
- autoSize, autoSizeChildren `: Boolean`

### GuideGridNode `: ObjectNode`
- stroke `: Paint`

### ImageNode `: ObjectNode`
- x `: Number`
- y `: Number`
- width, fitWidth `: Number`
- height, fitHeight `: Number`
- ratio, preserveRatio `: Boolean`
- smooth `: Boolean`

### LineNode `: ShapeNode`
- startX `: Number`
- startY `: Number`
- endX `: Number`
- endY `: Number`

### PolygonNode `: ShapeNode`

### PolylineNode `: ShapeNode`

### RectangleNode `: ShapeNode`
- x `: Number`
- y `: Number`
- width `: Number`
- height `: Number`
- arcWidth `: Number`
- arcHeight `: Number`

### SVGPathNode `: ShapeNode`
- content `: String`
- fillRule `: Enum (EVEN_ODD=0, NON_ZERO=1)`

### TextNode `: ShapeNode`
- x `: Number`
- y `: Number`
- text `: String`
- font `: Font`
- lineSpacing `: Number`
- wrappingWidth `: Number`
- strike, strikethrough `: Boolean`
- underline `: Boolean`
- boundsType `: Enum (LOGICAL=0, VISUAL=1, LOGICAL_VERTICAL_CENTER=2)`
- fontSmoothingType `: Enum (GRAY=0, LCD=1)`
- halign, textAlignment `: Enum (LEFT=0, CENTER=1, RIGHT=2, JUSTIFY=3)`
- valign, textOrigin `: Enum (TOP=0, CENTER=1, BASELINE=2, BOTTOM=3)`

### TextFlowNode `: RegionNode`
- lineSpacing `: Number`
- tabSize `: Number`
- halign, textAlignment `: Enum (LEFT=0, CENTER=1, RIGHT=2, JUSTIFY=3)`
