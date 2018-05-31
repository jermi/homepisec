import * as React from 'react';
import ThermIcon from './therm.svg';
import InfoIcon from './baseline-info-24px.svg';
import {ReactHTMLElement, ReactNode} from "react";

export const Info = (props: any) => <img src={InfoIcon} {...props} /> ;
export const Therm = (props: any) => <img src={ThermIcon} {...props} />;