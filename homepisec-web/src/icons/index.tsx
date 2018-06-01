import * as React from 'react';

// workaround handling of SVG files by CRA

import ThermIconSvgHtml from '!raw-loader!./therm.svg';
export const ThermIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:ThermIconSvgHtml}} {...props}/>;
import InfoIconSvgHtml from '!raw-loader!./baseline-info-24px.svg';
export const InfoIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:InfoIconSvgHtml}} {...props}/>;
import RelayOnIconSvgHtml from "!raw-loader!./baseline-power-24px.svg";
export const RelayOnIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:RelayOnIconSvgHtml}} {...props}/>;
import RelayOffIconSvgHtml from "!raw-loader!./baseline-power_off-24px.svg";
export const RelayOffIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:RelayOffIconSvgHtml}} {...props}/>;
import MotionIconSvgHtml from "!raw-loader!./baseline-rss_feed-24px.svg";
export const MotionIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:MotionIconSvgHtml}} {...props}/>;

import OkIconSvgHtml from '!raw-loader!./baseline-check-24px.svg';
export const OkIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:OkIconSvgHtml}} {...props}/>;
import WarningIconSvgHtml from '!raw-loader!./baseline-warning-24px.svg';
export const WarningIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:WarningIconSvgHtml}} {...props}/>;
import ErrorIconSvgHtml from '!raw-loader!./baseline-error-24px.svg';
export const ErrorIcon: React.SFC<React.DetailedHTMLProps<React.HTMLAttributes<any>, any>> = (props: any) => <span dangerouslySetInnerHTML={{__html:ErrorIconSvgHtml}} {...props}/>;