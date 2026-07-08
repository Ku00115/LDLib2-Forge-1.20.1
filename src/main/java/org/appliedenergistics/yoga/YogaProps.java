/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.appliedenergistics.yoga;

import org.appliedenergistics.yoga.style.StyleLength;
import org.appliedenergistics.yoga.style.StyleSizeLength;

public interface YogaProps {

    /* Width properties */

    void setWidth(StyleSizeLength length);

    default void setWidth(float width) {
        setWidth(StyleSizeLength.points(width));
    }

    default void setWidthPercent(float percent) {
        setWidth(StyleSizeLength.percent(percent));
    }

    default void setWidthAuto() {
        setWidth(StyleSizeLength.ofAuto());
    }

    default void setWidthMaxContent() {
        setWidth(StyleSizeLength.ofMaxContent());
    }

    default void setWidthFitContent() {
        setWidth(StyleSizeLength.ofFitContent());
    }

    default void setWidthStretch() {
        setWidth(StyleSizeLength.ofStretch());
    }

    void setMinWidth(StyleSizeLength length);

    default void setMinWidth(float minWidth) {
        setMinWidth(StyleSizeLength.points(minWidth));
    }

    default void setMinWidthPercent(float percent) {
        setMinWidth(StyleSizeLength.percent(percent));
    }

    default void setMinWidthMaxContent() {
        setMinWidth(StyleSizeLength.ofMaxContent());
    }

    default void setMinWidthFitContent() {
        setMinWidth(StyleSizeLength.ofFitContent());
    }

    default void setMinWidthStretch() {
        setMinWidth(StyleSizeLength.ofStretch());
    }

    void setMaxWidth(StyleSizeLength length);

    default void setMaxWidth(float maxWidth) {
        setMaxWidth(StyleSizeLength.points(maxWidth));
    }

    default void setMaxWidthPercent(float percent) {
        setMaxWidth(StyleSizeLength.percent(percent));
    }

    default void setMaxWidthMaxContent() {
        setMaxWidth(StyleSizeLength.ofMaxContent());
    }

    default void setMaxWidthFitContent() {
        setMaxWidth(StyleSizeLength.ofFitContent());
    }

    default void setMaxWidthStretch() {
        setMaxWidth(StyleSizeLength.ofStretch());
    }

    /* Height properties */

    void setHeight(StyleSizeLength length);

    default void setHeight(float height) {
        setHeight(StyleSizeLength.points(height));
    }

    default void setHeightPercent(float percent) {
        setHeight(StyleSizeLength.percent(percent));
    }

    default void setHeightAuto() {
        setHeight(StyleSizeLength.ofAuto());
    }

    default void setHeightMaxContent() {
        setHeight(StyleSizeLength.ofMaxContent());
    }

    default void setHeightFitContent() {
        setHeight(StyleSizeLength.ofFitContent());
    }

    default void setHeightStretch() {
        setHeight(StyleSizeLength.ofStretch());
    }

    void setMinHeight(StyleSizeLength length);

    default void setMinHeight(float minHeight) {
        setMinHeight(StyleSizeLength.points(minHeight));
    }

    default void setMinHeightPercent(float percent) {
        setMinHeight(StyleSizeLength.percent(percent));
    }

    default void setMinHeightMaxContent() {
        setMinHeight(StyleSizeLength.ofMaxContent());
    }

    default void setMinHeightFitContent() {
        setMinHeight(StyleSizeLength.ofFitContent());
    }

    default void setMinHeightStretch() {
        setMinHeight(StyleSizeLength.ofStretch());
    }

    void setMaxHeight(StyleSizeLength length);

    default void setMaxHeight(float maxHeight) {
        setMaxHeight(StyleSizeLength.points(maxHeight));
    }

    default void setMaxHeightPercent(float percent) {
        setMaxHeight(StyleSizeLength.percent(percent));
    }

    default void setMaxHeightMaxContent() {
        setMaxHeight(StyleSizeLength.ofMaxContent());
    }

    default void setMaxHeightFitContent() {
        setMaxHeight(StyleSizeLength.ofFitContent());
    }

    default void setMaxHeightStretch() {
        setMaxHeight(StyleSizeLength.ofStretch());
    }

    /* Margin properties */

    void setMargin(YogaEdge edge, StyleLength length);

    default void setMargin(YogaEdge edge, float margin) {
        setMargin(edge, StyleLength.points(margin));
    }

    default void setMarginPercent(YogaEdge edge, float percent) {
        setMargin(edge, StyleLength.percent(percent));
    }

    default void setMarginAuto(YogaEdge edge) {
        setMargin(edge, StyleLength.ofAuto());
    }

    /* Padding properties */

    void setPadding(YogaEdge edge, StyleLength length);

    default void setPadding(YogaEdge edge, float padding) {
        setPadding(edge, StyleLength.points(padding));
    }

    default void setPaddingPercent(YogaEdge edge, float percent) {
        setPadding(edge, StyleLength.percent(percent));
    }

    /* Position properties */

    void setPositionType(YogaPositionType positionType);

    void setPosition(YogaEdge edge, StyleLength length);

    default void setPosition(YogaEdge edge, float position) {
        setPosition(edge, StyleLength.points(position));
    }

    default void setPositionPercent(YogaEdge edge, float percent) {
        setPosition(edge, StyleLength.percent(percent));
    }

    default void setPositionAuto(YogaEdge edge) {
        setPosition(edge, StyleLength.ofAuto());
    }

    /* Alignment properties */

    void setAlignContent(YogaAlign alignContent);

    void setAlignItems(YogaAlign alignItems);

    void setAlignSelf(YogaAlign alignSelf);

    /* Flex properties */

    void setFlex(float flex);

    void setFlexBasisAuto();

    void setFlexBasisPercent(float percent);

    void setFlexBasis(float flexBasis);

    void setFlexBasisMaxContent();

    void setFlexBasisFitContent();

    void setFlexBasisStretch();

    void setFlexDirection(YogaFlexDirection direction);

    void setFlexGrow(float flexGrow);

    void setFlexShrink(float flexShrink);

    /* Other properties */

    void setJustifyContent(YogaJustify justifyContent);

    void setDirection(YogaDirection direction);

    void setBorder(YogaEdge edge, float value);

    void setWrap(YogaWrap wrap);

    void setAspectRatio(float aspectRatio);

    void setIsReferenceBaseline(boolean isReferenceBaseline);

    void setMeasureFunction(YogaMeasureFunction measureFunction);

    void setBaselineFunction(YogaBaselineFunction yogaBaselineFunction);

    void setBoxSizing(YogaBoxSizing boxSizing);

    /* Getters */

    YogaValue getWidth();

    YogaValue getMinWidth();

    YogaValue getMaxWidth();

    YogaValue getHeight();

    YogaValue getMinHeight();

    YogaValue getMaxHeight();

    YogaDirection getStyleDirection();

    YogaFlexDirection getFlexDirection();

    YogaJustify getJustifyContent();

    YogaAlign getAlignItems();

    YogaAlign getAlignSelf();

    YogaAlign getAlignContent();

    YogaPositionType getPositionType();

    float getFlexGrow();

    float getFlexShrink();

    YogaValue getFlexBasis();

    float getAspectRatio();

    YogaValue getMargin(YogaEdge edge);

    YogaValue getPadding(YogaEdge edge);

    YogaValue getPosition(YogaEdge edge);

    float getBorder(YogaEdge edge);

    YogaValue getGap(YogaGutter gutter);

    void setGap(YogaGutter gutter, StyleLength value);

    default void setGap(YogaGutter gutter, float value) {
        setGap(gutter, StyleLength.points(value));
    }

    default void setGapPercent(YogaGutter gutter, float percent) {
        setGap(gutter, StyleLength.percent(percent));
    }

    YogaBoxSizing getBoxSizing();
}
