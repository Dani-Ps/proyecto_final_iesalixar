import React from "react";
import './style.scss';
import { Link } from 'react-router-dom';

const FooterComponent = () => {

    return (
        <>
            <div className="footer-container">
                <div className="row-1">
                    <Link to="#"><span>About </span></Link>
                    <Link to="#"><span>Blog </span></Link>
                    <Link to=""><span>jobs </span></Link>
                    <Link to="#"><span>Help </span></Link>
                    <Link to="#"><span>API </span></Link>
                    <Link to="#"><span>Privacy </span></Link>
                    <Link to="#"><span>Terms </span></Link>
                    <Link to="#"><span>Top Accounts </span></Link>
                    <Link to="#"><span>locations </span></Link>
                </div>
                <div className="row-2">
                    <Link to="#"><span>2024 FitConnet</span></Link>
                </div>
            </div>
        </>
    );

};
export default FooterComponent;