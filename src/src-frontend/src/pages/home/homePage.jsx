import React, { useState, useEffect } from 'react';
import FooterComponent from '../../components/footer/footer';
import { DefaultHeader, IconHeader } from '../../components/header/headers';
import SidebarComponent from '../../components/sidebar/sidebar';
import Skeleton from '../../components/skeleton/skeleton';
import Logoicon from '../../components/logo/logoIcon';
import Logotext from '../../components/logo/logotext';

const HomePage = () => {

    const [isSmallScreen, setIsSmallScreen] = useState(false);

    const checkScreenSize = () => {
        const isSmall = window.innerWidth <= 500;
        setIsSmallScreen(isSmall);
    };
    useEffect(() => {
        checkScreenSize();
        window.addEventListener('resize', checkScreenSize);
        return () => {
            window.removeEventListener('resize', checkScreenSize);
        };
    }, []);
    return (

        <Skeleton
            mainContent={
                <>
                    {isSmallScreen ? (
                        <IconHeader
                            leftContent={<Logoicon />}
                            rightContent={<div></div>}
                        />
                    ) : (
                        <DefaultHeader
                            leftContent={<Logotext />}
                        />
                    )}
                    <SidebarComponent />
                </>
            }
            footerContent={<FooterComponent />}
        />
    );
};
export default HomePage;
