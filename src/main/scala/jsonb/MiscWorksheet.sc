
val STATE_CONTAINS = "SELECT COUNT(*) " + "    FROM   cbs_mart.state_boundary mpg" + "    WHERE  SDO_CONTAINS(mpg.boundary, " + "              sdo_geometry(2001, 8307, " + "                mdsys.sdo_point_type(?, ?, NULL),NULL, NULL)) = 'TRUE'" + "    AND state = ?"
